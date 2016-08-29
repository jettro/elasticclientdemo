import {OnInit, Component} from "@angular/core";
import {EmployeeService} from "../services/employee.service";
import {Employee} from "../services/employee";
import {Observable, Subject} from "rxjs";
import {Router} from "@angular/router";

@Component({
    selector: "my-employee-search",
    styles: [require('./employee-search.css')],
    template: require('./employee-search.html'),
    providers: [EmployeeService]
})
export class EmployeeSearchComponent implements OnInit {
    employees: Observable<Employee[]>;

    searchTerm: string;

    private searchTerms = new Subject<string>();

    constructor(private employeeService: EmployeeService,
                private router: Router) {
    };

    ngOnInit(): void {
        this.employees = this.searchTerms
            .debounceTime(300)
            .distinctUntilChanged()
            .switchMap(term => term
                ? this.employeeService.searchEmployee(term)
                : Observable.of<Employee[]>([]))
            .catch(error => {
                console.log(error);
                return Observable.of<Employee[]>([]);
            });
    }

    search(event: any): void {
        this.searchTerms.next(event);
    }

    gotoDetail(employee: Employee): void {
        let link = ['/employee', employee.id];
        this.router.navigate(link);
    }

}
