import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { RouteReuseStrategy } from "@angular/router";

import { IonicModule, IonicRouteStrategy } from "@ionic/angular";
import { SplashScreen } from "@ionic-native/splash-screen/ngx";
import { StatusBar } from "@ionic-native/status-bar/ngx";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { Insomnia } from "@ionic-native/insomnia/ngx";

import { HttpClientModule } from "@angular/common/http";
import {
  HttpCacheInterceptorModule,
  useHttpCacheLocalStorage,
} from "@ngneat/cashew";
import { IonicStorageModule } from "@ionic/storage";
@NgModule({
  declarations: [AppComponent],
  entryComponents: [],
  imports: [
    BrowserModule,
    HttpClientModule,
    IonicModule.forRoot(),
    IonicStorageModule.forRoot(),
    AppRoutingModule,
    HttpCacheInterceptorModule.forRoot(),
  ],
  providers: [
    StatusBar,
    Insomnia,
    useHttpCacheLocalStorage,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
