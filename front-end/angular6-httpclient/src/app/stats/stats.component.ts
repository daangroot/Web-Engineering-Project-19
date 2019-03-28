import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {

  stats:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getStats();
  }

  getStats() {
    this.stats = [];
    this.rest.getStatsAtAirport(this.route.snapshot.params['airportCode', 'carrierCode']).subscribe((data: {}) => {
      console.log(data);
      this.stats = data;
    });
  }
}
