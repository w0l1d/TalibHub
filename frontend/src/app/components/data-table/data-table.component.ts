import { Component } from "@angular/core";
import { NgForOf, NgIf } from "@angular/common";
import { DataTablesModule } from "angular-datatables";
import { HttpClient } from "@angular/common/http";
import { environment as env } from "../../../environments/environment.development";

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


  constructor(private http: HttpClient) {
    this.baseUrl = env.api;
  }

  ngOnInit(): void {
    this.dtOptions = {
      serverSide: true,     // Set the flag
      ajax: (dataTablesParameters: any, callback) => {
        this.http
          .post<any>(
            `${this.baseUrl}/students/data`,
            dataTablesParameters, {}
          ).subscribe(resp => {
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: resp.data
          });
        });
      },
      columns: [{
        title: "ID",
        data: "id"
      }, {
        title: "First name",
        data: "firstName"
      }, {
        title: "Last name",
        data: "lastName"
      }]
    };

  }


}
