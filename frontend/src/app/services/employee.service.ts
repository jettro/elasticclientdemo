import {Employee} from "./employee";
import {Logger} from "../logger.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';

@Injectable()
export class EmployeeService {
    constructor(private logger: Logger, private http: Http) {
    }

    findEmployees(): Promise<Employee[]> {
        this.logger.log("returning all Employees");

        return this.http.get("http://localhost:8080/api/employee")
            .toPromise()
            .then(response => response.json() as Employee[])
            .catch(this.handleError);
    }

    findEmployee(id: string): Promise<Employee> {
        return this.http.get("http://localhost:8080/api/employee/"+id)
            .toPromise()
            .then(response => response.json() as Employee)
            .catch(this.handleError)
    }

    handleError(error): void {
        this.logger.warn(error);
    }
}
