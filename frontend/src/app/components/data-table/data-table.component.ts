import { Component, OnInit } from "@angular/core";
import { NgForOf, NgIf } from "@angular/common";
import { DataTablesModule } from "angular-datatables";
import { HttpClient } from "@angular/common/http";
import { environment as env } from "../../../environments/environment.development";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: "app-data-table",
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DataTablesModule
  ],
  template: "<table datatable [dtOptions]=\"dtOptions\" class=\"table table-striped table-bordered table-hover\"></table>",
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
      columns: [{
        title: "CNE",
        data: "cne"
      }, {
        title: "Full Name",
        data: "firstName",
        render: (data: any, type: any, full: any) => data + " " + full.lastName
      }, {
        data: "lastName",
        visible: false
      }, {
        title: "Birth date",
        data: "birthDate",
        render: (data: any, type: any, full: any) => new Date(data).toLocaleDateString()
      }, {
        title: "Email",
        data: "email"
      }, {
        title: "Phone",
        data: "phone"
      }, {
        title: "Promotion",
        data: "enrollmentYear",
        render: (data: any, type: any, full: any) => data + " - " + (+data + 1)
      }, {
        title: "Enabled",
        data: "enabled",
        render: (data: any, type: any, full: any) =>
          `<span class="inline-block text-white px-2 py-1 text-xs rounded bg-${data ? "green" : "red"}-500">${data ? "Yes" : "No"}</span>`
      }, {
        title: "Actions",
        orderable: false,
        className: "all",
        data: "id",
        render: (data: any, type: any, full: any) =>
          `<a href="/students/${data}" class="px-2 py-1 bg-blue-500 text-white text-sm">Edit</a>`
      }]

    };

  }


}
