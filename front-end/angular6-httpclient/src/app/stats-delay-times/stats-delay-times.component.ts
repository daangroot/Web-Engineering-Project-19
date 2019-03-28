import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats-delay-times',
  templateUrl: './stats-delay-times.component.html',
  styleUrls: ['./stats-delay-times.component.css']
})
export class StatsDelayTimesComponent implements OnInit {

  statsTimes:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getStatsDelayTime();
  }

  getStatsDelayTime() {
    this.statsTimes = [];
    this.rest.getDelayTime().subscribe((data: {}) => {
      console.log(data);
      this.statsTimes = data;
    });
  }
}
