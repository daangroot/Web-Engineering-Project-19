import { Component, OnInit, Input } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats-edit',
  templateUrl: './stats-edit.component.html',
  styleUrls: ['./stats-edit.component.css']
})

export class StatsEditComponent implements OnInit {

  @Input() statsData:any = { flightData: '', delayData: '', delayTimeData:'' };

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.rest.updateStats(this.route.snapshot.params['airportCode'],['carrierCode'],this.statsData).subscribe((data: {}) => {
      console.log(data);
      this.statsData = data;
    });
  }

  updateStats() {
    this.rest.updateStats(this.route.snapshot.params['airportCode'],['carrierCode'], this.statsData).subscribe((result) => {
      this.router.navigate(['/airports/' + result._airportCode + '/carriers/' + result._carrierCode + '/stats/']);
    }, (err) => {
      console.log(err);
    });
  }

}
