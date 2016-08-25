import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {Logger} from "./logger.service";
import {EmployeeService} from "./services/employee.service";
import {AppComponent} from "./app.component";
import {routing} from "./app.routing";
import {HomeComponent} from "./home/home.component";
import {AboutComponent} from "./about/about.component";
import {FormsModule} from "@angular/forms";
import {EmployeeDetailComponent} from "./employee/employee-detail.component";
import {EmployeesComponent} from "./employee/employees.component";
import {HttpModule} from "@angular/http";

@NgModule({
    imports: [BrowserModule, FormsModule, routing, HttpModule],
    providers: [Logger, EmployeeService],
    declarations: [AppComponent, HomeComponent, AboutComponent, EmployeesComponent, EmployeeDetailComponent],
    bootstrap: [AppComponent]
})
export class AppModule {
}
