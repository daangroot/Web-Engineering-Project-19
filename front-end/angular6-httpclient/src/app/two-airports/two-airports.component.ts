import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-two-airports',
  templateUrl: './two-airports.component.html',
  styleUrls: ['./two-airports.component.css']
})
export class TwoAirportsComponent implements OnInit {

  airports:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.get2Airports();
  }

  get2Airports() {
    this.airports = [];
    this.rest.get2Airports(this.route.snapshot.params['airportCode1', 'airportCode2']).subscribe((data: {}) => {
      console.log(data);
      this.airports = data;
    });
  }
}
