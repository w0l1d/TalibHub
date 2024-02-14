import {Component, inject} from "@angular/core";
import {LayoutComponent} from "../../components/layout/layout.component";
import {DataTableComponent} from "../../components/data-table/data-table.component";
import Student from "../../models/student";
import {StudentService} from "../../services/student.service";
import {HttpClientModule} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import NavbarLink from "../../models/NavbarLink";


@Component({
  selector: "app-student-management",
  standalone: true,
  imports: [
    LayoutComponent,
    DataTableComponent,
    HttpClientModule
  ],
  providers: [
    StudentService
  ],
  templateUrl: "./student-management.component.html",
  styleUrl: "./student-management.component.css"
})
export class StudentManagementComponent {
  navbarData: NavbarLink[] | undefined;

  constructor(
    private studentService: StudentService
  ) {
    this.setNavbarData();
  }

  saveStudentsAsCsv(): void {
    console.log("students saved as csv");
  }

  //parse csv file when dropped
  parseCsvFile(event: Event): void {
    console.log(event);
    console.log("files dropped successfully");
    const inputElement = event.target as HTMLInputElement;
    const file = inputElement.files?.[0];

    if (file) {
      const reader = new FileReader();

      reader.onload = (e) => {
        const csvContent = e.target?.result as string;
        //console.log(csvContent);
        //skip the first line
        const lines: string[] = csvContent.split("\n").slice(1);
        //delete the last line if it's empty
        if (lines[lines.length - 1] === "") {
          lines.pop();
        }
        console.log(lines);
        //create an array of students
        const students: Student[] = [];
        for (const line of lines) {
          const values: string[] = line.split(",");
          //split values[6] into day, month and year
          console.log("line values : ", values);
          console.log("date : ", values[6]);

          const date: string[] = values[6].split("/");
          console.log(date);
          const student = new Student(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
            true,
            values[5],
            new Date(+date[2], +date[1], +date[0]),
            +values[7],
            2024
          );
          students.push(student);
        }

        console.log(students);
        //save students
        this.studentService.saveAllStudents(students).subscribe(
          (data:any):void => {
            console.log(data);
          }
        );
      };

      reader.readAsText(file);
    } else {
      console.error("No file selected");
    }
  }
  private setNavbarData(): void {
    this.navbarData = [
      { route: '/home', icon: 'fa-solid fa-house', label: 'Home' , highlight: false},
      { route: '/search', icon: 'fa-solid fa-search', label: 'Search', highlight: false},
      { route: '/news', icon: 'fa-solid fa-newspaper', label: 'News', highlight: false },
      { route: '/logout', icon: 'fa-solid fa-right-from-bracket', label: 'Logout', highlight: false },
    ];

    if (inject(AuthService).isManager()) {
      this.navbarData.splice(2, 0, { route: '/studentManagement', icon: 'fa-sharp fa-solid fa-shield-halved', label: 'Admin', highlight: true });
    } else if (inject(AuthService).isStudent()) {
      this.navbarData.splice(1, 0, { route: '/studentProfile', icon: 'fa-solid fa-user', label: 'Profile', highlight: false });
    }
  }

}
