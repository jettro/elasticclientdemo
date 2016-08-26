import {Component, OnInit} from "@angular/core";
import {Employee} from "../services/employee";
import {ActivatedRoute, Params} from "@angular/router";
import {EmployeeService} from "../services/employee.service";

@Component({
    selector: "my-employee-detail",
    styles: [require('./employee-detail.css')],
    template: require('./employee-detail.html'),
})
export class EmployeeDetailComponent implements OnInit {
    employee: Employee;
    error: any;

    constructor(private route: ActivatedRoute, private employeeService: EmployeeService) {
        console.log("CREATE DETAIL COMPONENT");
    }

    ngOnInit(): void {
        this.route.params.forEach((params: Params) => {
            if (params['id'] !== undefined) {
                console.log(params['id']);
                let id = params['id'];
                this.employeeService.findEmployee(id)
                    .then(employee => {
                        this.employee = employee;
                        console.log(this.employee);
                    });
            } else {
                console.log("Create new Employee");
                this.employee = new Employee();
            }
        });
    }

    goBack(savedEmployee: Employee = null): void {
        // this.close.emit(savedEmployee);
        window.history.back();
    }

    save(): void {
        console.log(this.employee);
        this.employeeService
            .storeEmployee(this.employee)
            .then(employee => {
                this.employee = employee; // saved hero, w/ id if new
                this.goBack(employee);
            })
            .catch(error => this.error = error); // TODO: Display error message
    }
}
