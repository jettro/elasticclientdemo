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
    error: any;

    constructor(private employeeService : EmployeeService, private router: Router) {}

    gotoDetail(employee: Employee) {
        let link = ['/employee', employee.id];
        this.router.navigate(link);
    }

    deleteEmployee(employee: Employee, event: any): void {
        event.stopPropagation();
        this.employeeService
            .removeEmployee(employee)
            .then(() => {
                this.loadEmployees();
            })
            .catch(error => this.error = error);
    }

    addEmployee(): void {
        let link = ['/employee/'];
        this.router.navigate(link);
    }

    ngOnInit(): void {
        this.loadEmployees();
    }

    loadEmployees(): void {
        this.employeeService.findEmployees().then(employees => {
            this.employees = employees;
        });
    }
}
