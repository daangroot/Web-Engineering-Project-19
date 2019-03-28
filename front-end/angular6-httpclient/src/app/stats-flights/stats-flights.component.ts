import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats-flights',
  templateUrl: './stats-flights.component.html',
  styleUrls: ['./stats-flights.component.css']
})
export class StatsFlightsComponent implements OnInit {

  statsFlights:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getStatsFlights();
  }

  getStatsFlights() {
    this.statsFlights = [];
    this.rest.getFlightData(this.route.snapshot.params['airportCode'],['carrierCode']).subscribe((data: {}) => {
      console.log(data);
      this.statsFlights = data;
    });
  }
}
