import { Component } from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent {
  students = [
    {
      name: 'John Doe',
      birthdate: '01/01/2000',
      email: 'john@email.com',
      level: 'Graduated',
      address: '123 Main St, City, State, 12345',
      online: false
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: false
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    },
    {
      name: 'Jane Doe',
      birthdate: '01/01/2000',
      email: 'jane@email.com',
      level: 'ILISI 3',
      online: true
    }
  ];

}
