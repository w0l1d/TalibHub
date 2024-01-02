import { Component } from "@angular/core";
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
  templateUrl: "./data-table.component.html",
  styleUrl: "./data-table.component.css"
})
export class DataTableComponent {

  dtOptions: DataTables.Settings = {};
  private readonly baseUrl: string;


  constructor(private http: HttpClient, private authService: AuthService) {
    this.baseUrl = env.api;
  }

  ngOnInit(): void {
    this.dtOptions = {
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
        render: function(data: any, type: any, full: any) {
          return data + " " + full.lastName;
        }
      }, {
        data: "lastName",
        visible: false
      }, {
        title: "Birth date",
        data: "birthDate",
        render: function(data: any, type: any, full: any) {
          return new Date(data).toLocaleDateString();
        }
      }, {
        title: "Email",
        data: "email"
      }, {
        title: "Phone",
        data: "phone"
      }, {
        title: "Promotion",
        data: "enrollmentYear",
        render: function(data: any, type: any, full: any) {
          return data + " - " + (data + 1);
        }
      }, {
        title: "Enabled",
        data: "enabled",
        render: function(data: any, type: any, full: any) {
          return `<span class="enabled-badge ${data ? "enabled" : "disabled"}">${data ? "Yes" : "No"}</span>`;
        }
      }, {
        title: "Actions",
        data: "id",
        render: function(data: any, type: any, full: any) {
          return `<a href="/students/${data}" class="action-btn">Edit</a>`;
        }
      }]

    };

  }


}
