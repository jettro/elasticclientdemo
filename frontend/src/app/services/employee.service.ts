import {Employee} from "./employee";
import {Logger} from "../logger.service";
import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Observable} from "rxjs";

@Injectable()
export class EmployeeService {
    private employeeUrl: string = "http://localhost:8080/api/employee";

    constructor(private logger: Logger, private http: Http) {
    }

    searchEmployee(term: string): Observable<Employee[]> {
        return this.http
            .get(`${this.employeeUrl}/_search?term=${term}`)
            .map((r: Response) => r.json() as Employee[]);
    }

    findEmployees(): Promise<Employee[]> {
        this.logger.log("returning all Employees");

        return this.http.get(this.employeeUrl)
            .toPromise()
            .then(response => response.json() as Employee[])
            .catch(this.handleError);
    }

    findEmployee(id: string): Promise<Employee> {
        let url = `${this.employeeUrl}/${id}`;
        return this.http.get(url)
            .toPromise()
            .then(response => response.json() as Employee)
            .catch(this.handleError)
    }

    storeEmployee(employee: Employee): Promise<Employee> {
        let headers = new Headers({
            'Content-Type': 'application/json'
        });
        return this.http
            .post(this.employeeUrl, JSON.stringify(employee), {headers: headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    removeEmployee(employee: Employee): Promise<Response> {
        let headers = new Headers({
            'Content-Type': 'application/json'
        });

        let url = `${this.employeeUrl}/${employee.id}`;

        return this.http
            .delete(url)
            .toPromise()
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
