import {Component} from '@angular/core';
import {MdIconRegistry} from "@angular2-material/icon";

@Component({
    selector: 'my-app',
    styles: [require('./app.css')],
    template: require('./app.html'),
    providers: [MdIconRegistry]
})
export class AppComponent {}
