import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';

import { MainComponent } from './main/main.component';
import { AirportsComponent } from './airports/airports.component';
import { CarriersComponent } from './carriers/carriers.component';
import { StatsComponent } from './stats/stats.component';
import { FlightStatisticsComponent } from './flight-statistics/flight-statistics.component';
import { DelayTimeStatisticsComponent } from './delay-time-statistics/delay-time-statistics.component';
import { ExtraStatisticsComponent } from './extra-statistics/extra-statistics.component';

const appRoutes: Routes = [
  {
    path: '',
    component: MainComponent,
    data: { title: 'root' }
  },
  {
    path: 'airports',
    component: AirportsComponent,
    data: { title: 'Airports' }
  },
  {
    path: 'carriers',
    component: CarriersComponent,
    data: { title: 'Carriers' }
  },
  {
    path: 'statistics',
    component: StatsComponent,
    data: { title: 'Statistics' }
  },
  {
    path: 'statistics/flight',
    component: FlightStatisticsComponent,
    data: { title: 'Flight Statistics' }
  },
  {
    path: 'statistics/delay-time',
    component: DelayTimeStatisticsComponent,
    data: { title: 'Delay Time Statistics' }
  },
  {
    path: 'extra-statistics',
    component: ExtraStatisticsComponent,
    data: { title: 'Delay Time Statistics' }
  }
];

@NgModule({
  declarations: [
    AppComponent,
    AirportsComponent,
    CarriersComponent,
    StatsComponent,
    MainComponent,
    FlightStatisticsComponent,
    DelayTimeStatisticsComponent,
    ExtraStatisticsComponent
  ],
  imports: [
  RouterModule.forRoot(appRoutes),
  FormsModule,
  BrowserModule,
  HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
