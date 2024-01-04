import { Component, OnInit } from "@angular/core";
import { NgForOf, NgIf } from "@angular/common";
import { DataTablesModule } from "angular-datatables";
import { HttpClient } from "@angular/common/http";
import { environment as env } from "../../../environments/environment.development";
import { AuthService } from "../../services/auth.service";
import studentTableColumns from "./student-table-columns";

@Component({
  selector: "app-data-table",
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DataTablesModule
  ],
  template: "<table datatable [dtOptions]=\"dtOptions\" class=\"table table-striped nowrap\" style=\"width:100%\"></table>",
  styleUrl: "./data-table.component.css"
})
export class DataTableComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
  private readonly baseUrl: string;

  constructor(private http: HttpClient, private authService: AuthService) {
    this.baseUrl = env.api;
  }

  ngOnInit(): void {
    this.dtOptions = {
      responsive: true,

      serverSide: true,  // Set the flag
      ajax: (dataTablesParameters: any, callback) => {
        this.http
          .post<any>(
            `${this.baseUrl}/students/data`,
            dataTablesParameters, {
              headers: {
                Authorization: `Bearer ${this.authService.getJwtToken()}`
              }
            }
          ).subscribe(resp => {
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: resp.data
          });
        });
      },
      columns: studentTableColumns
    };

  }


}
