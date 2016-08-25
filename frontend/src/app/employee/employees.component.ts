import {Component, OnInit} from '@angular/core';
import {EmployeeService} from "../services/employee.service";
import {Employee} from "../services/employee";
import {Router} from "@angular/router";

@Component({
    selector: 'my-employees',
    styles: [require('./employees.css')],
    template: require('./employees.html'),
    providers: [EmployeeService]
})
export class EmployeesComponent implements OnInit {
    employees : Employee[];

    constructor(private employeeService : EmployeeService, private router: Router) {}

    gotoDetail(employee: Employee) {
        let link = ['/employee', employee.id];
        this.router.navigate(link);
    }

    ngOnInit(): void {
        this.employeeService.findEmployees().then(employees => {
            console.log(employees);
            this.employees = employees;
        });
    }
}
