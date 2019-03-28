import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-carrier-detail',
  templateUrl: './carrier-detail.component.html',
  styleUrls: ['./carrier-detail.component.css']
})
export class CarrierDetailComponent implements OnInit {

  carrier:any;

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.rest.getAirportCarrier(this.route.snapshot.params['airportCode','carrierCode']).subscribe((data: {}) => {
      console.log(data);
      this.carrier = data;
    });
  }

}
