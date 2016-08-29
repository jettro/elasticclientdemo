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
import {EmployeeSearchComponent} from "./employee/employee-search.component";
import {MdCardModule} from "@angular2-material/card";
import {MdToolbarModule} from "@angular2-material/toolbar";
import {MdSidenavModule} from "@angular2-material/sidenav";
import {MdIconModule} from "@angular2-material/icon";
import {MdListModule} from "@angular2-material/list";
import {MdButtonModule} from "@angular2-material/button";
import {MdInputModule} from "@angular2-material/input";
import {SearchComponent} from "./search/search.component";

@NgModule({
    imports: [BrowserModule, FormsModule, routing, HttpModule, MdCardModule, MdToolbarModule, MdSidenavModule, MdIconModule, MdListModule, MdButtonModule, MdInputModule],
    providers: [Logger, EmployeeService],
    declarations: [
        AppComponent,
        HomeComponent,
        AboutComponent,
        EmployeesComponent,
        EmployeeDetailComponent,
        EmployeeSearchComponent,
        SearchComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
