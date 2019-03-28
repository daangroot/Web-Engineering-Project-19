import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats-delay-times-airport',
  templateUrl: './stats-delay-times-airport.component.html',
  styleUrls: ['./stats-delay-times-airport.component.css']
})
export class StatsDelayTimesAirportComponent implements OnInit {

  statsTimes:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getStatsDelayTimeAirport();
  }

  getStatsDelayTimeAirport() {
    this.statsTimes = [];
    this.rest.getDelayTimeAirport(this.route.snapshot.params['airportCode']).subscribe((data: {}) => {
      console.log(data);
      this.statsTimes = data;
    });
  }
}
