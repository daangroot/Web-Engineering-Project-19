import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { StatsDelayTimesAirportComponent } from './stats-delay-times-airport/stats-delay-times-airport.component';

import { AirportComponent } from './airport/airport.component';
import { AirportDetailComponent } from './airport-detail/airport-detail.component';

import { CarrierComponent } from './carrier/carrier.component';
import { CarrierDetailComponent } from './carrier-detail/carrier-detail.component';

import { CarriersAirportComponent } from './carriers-airport/carriers-airport.component';

import { StatsComponent } from './stats/stats.component';
import { StatsAddComponent } from './stats-add/stats-add.component';
import { StatsEditComponent } from './stats-edit/stats-edit.component';
import { StatsFlightsComponent } from './stats-flights/stats-flights.component';
import { StatsDelayTimesComponent } from './stats-delay-times/stats-delay-times.component';

import { TwoAirportsComponent } from './two-airports/two-airports.component';
import { TwoAirportsCarrierComponent } from './two-airports-carrier/two-airports-carrier.component';

import { ExtrastatsComponent } from './extrastats/extrastats.component';
import { ExtrastatsCarrierComponent } from './extrastats-carrier/extrastats-carrier.component';


const appRoutes: Routes = [
  {
    path: 'airports',
    component: AirportComponent,
    data: { title: 'Airports List' }
  },
  {
    path: 'airports/:airportCode',
    component: AirportDetailComponent,
    data: { title: 'Airport Details' }
  },
  {
    path: 'carriers',
    component: CarrierComponent,
    data: { title: 'Carrier List' }
  },
  {
    path: 'airports/:airportCode/carriers',
    component: CarriersAirportComponent,
    data: { title: 'Carriers at Airport' }
  },
  {
    path: 'airports/:airportCode/carriers/:carrierCode',
    component: CarrierDetailComponent,
    data: { title: 'Carrier Details' }
  },
  {
    path: 'airports/:airportCode/carriers/:carrierCode/stats',
    component: StatsComponent,
    data: { title: 'Stats for carrier at airport' }
  },
  {
    path: 'airports/carriers/stats',
    component: StatsAddComponent,
    data: { title: 'Add stats' }
  },
  {
    path: 'airports/:airportCode/carriers/:carrierCode/stats',
    component: StatsEditComponent,
    data: { title: 'Update stats' }
  },
  {
    path: 'airports/:airportCode/carriers/:carrierCode/stats/flight',
    component: StatsFlightsComponent,
    data: { title: 'Flight Details' }
  },
  {
    path: 'airports/:airportCode/carriers/stats/delay-time',
    component: StatsDelayTimesComponent,
    data: { title: 'Delay Times Details' }
  },
  {
    path: 'airports/:airportCode1/:airportCode2',
    component: TwoAirportsComponent,
    data: { title: '2 Airports List' }
  },
  {
    path: 'airports/:airportCode1/:airportCode2/carriers/:carrierCode',
    component: TwoAirportsCarrierComponent,
    data: { title: 'Carrier operating between two airports' }
  },
  {
    path: 'airports/:airportCode1/:airportCode2/carriers/extra-stats',
    component: ExtrastatsComponent,
    data: { title: 'Extra stats for all carriers between 2 airports' }
  },
  {
    path: 'airports/:airportCode1/:airportCode2/carriers/:carrierCode/extra-stats',
    component: ExtrastatsCarrierComponent,
    data: { title: 'Extra stats for carrier between 2 airports' }
  },
  { path: '',
    redirectTo: '/airports',
    pathMatch: 'full'
  }

];

@NgModule({
  declarations: [
    AppComponent,
    AirportComponent,
    AirportDetailComponent,
    CarrierComponent,
    CarrierDetailComponent,
    CarriersAirportComponent,
    ExtrastatsComponent,
    ExtrastatsCarrierComponent,
    StatsComponent,
    StatsAddComponent,
    StatsDelayTimesComponent,
    StatsDelayTimesAirportComponent,
    StatsEditComponent,
    StatsFlightsComponent,
    TwoAirportsComponent,
    TwoAirportsCarrierComponent
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
