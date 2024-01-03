import { Component } from "@angular/core";
import { LayoutComponent } from "../../components/layout/layout.component";
import { DataTableComponent } from "../../components/data-table/data-table.component";
import NavbarData from "./navbar-data";

@Component({
  selector: "app-student-management",
  standalone: true,
  imports: [
    LayoutComponent,
    DataTableComponent
  ],
  templateUrl: "./student-management.component.html",
  styleUrl: "./student-management.component.css"
})
export class StudentManagementComponent {
  navbarData = NavbarData;

  saveStudentsAsCsv(): void {

  }

  //parse csv file when dropped
  parseCsvFile(event: Response): void {
    console.log(event);
    console.log("files dropped successfully", event.ta);
  }

}
