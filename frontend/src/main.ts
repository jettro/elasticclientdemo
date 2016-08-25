import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {enableProdMode} from '@angular/core';

if (webpack.ENV === 'production') {
    enableProdMode();
}

import {AppModule} from './app/app.module';

platformBrowserDynamic().bootstrapModule(AppModule);