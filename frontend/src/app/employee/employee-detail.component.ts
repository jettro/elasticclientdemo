import {Component, Input, OnInit} from "@angular/core";
import {Employee} from "../services/employee";
import { ActivatedRoute, Params } from '@angular/router';
import {EmployeeService} from "../services/employee.service";

@Component({
    selector: "my-employee-detail",
    styles: [require('./employee-detail.css')],
    template: require('./employee-detail.html'),
})
export class EmployeeDetailComponent implements OnInit{
    employee: Employee;

    constructor(private route:ActivatedRoute, private employeeService:EmployeeService) {}

    ngOnInit(): void {
        this.route.params.forEach((param: Params) => {
            let id = param['id'];
            this.employeeService.findEmployee(id)
                .then(employee => this.employee = employee);

        });
    }

    goBack(): void {
        window.history.back();
    }
}
