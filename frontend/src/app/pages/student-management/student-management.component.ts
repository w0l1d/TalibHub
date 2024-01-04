import { Component } from "@angular/core";
import { LayoutComponent } from "../../components/layout/layout.component";
import { DataTableComponent } from "../../components/data-table/data-table.component";
import NavbarData from "./navbar-data";
import Student from "../../models/student";
import { StudentService } from "../../services/student.service";

@Component({
  selector: "app-student-management",
  standalone: true,
  imports: [
    LayoutComponent,
    DataTableComponent
  ],
  providers: [
    StudentService
  ],
  templateUrl: "./student-management.component.html",
  styleUrl: "./student-management.component.css"
})
export class StudentManagementComponent {
  navbarData = NavbarData;

  constructor(
    private studentService: StudentService
  ) {
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

}
