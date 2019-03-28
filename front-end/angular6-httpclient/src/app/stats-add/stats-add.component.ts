import { Component, OnInit, Input } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats-add',
  templateUrl: './stats-add.component.html',
  styleUrls: ['./stats-add.component.css']
})
export class StatsAddComponent implements OnInit {

  @Input() statsData = { airportCode:'', carrierCode: '', flightData: '', delayData: '', delayTimeData: '' };

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
  }

  addStats() {
    this.rest.addStats(this.statsData).subscribe((result) => {
      this.router.navigate(['/airports/' + result._airportCode + '/carriers/' + result._carrierCode + '/stats/']);
    }, (err) => {
      console.log(err);
    });
  }

}
