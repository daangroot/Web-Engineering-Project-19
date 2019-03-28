import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-two-airports-carrier',
  templateUrl: './two-airports-carrier.component.html',
  styleUrls: ['./two-airports-carrier.component.css']
})
export class TwoAirportsCarrierComponent implements OnInit {

  carriers:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.get2AirportsCarrier();
  }

  get2AirportsCarrier() {
    this.carriers = [];
    this.rest.get2AirportsCarrier(this.route.snapshot.params['airportCode1'],['airportCode2'],['carrierCode']).subscribe((data: {}) => {
      console.log(data);
      this.carriers = data;
    });
  }
}
