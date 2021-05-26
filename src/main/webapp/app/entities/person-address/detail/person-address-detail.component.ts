import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonAddress } from '../person-address.model';

@Component({
  selector: 'jhi-person-address-detail',
  templateUrl: './person-address-detail.component.html',
})
export class PersonAddressDetailComponent implements OnInit {
  personAddress: IPersonAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personAddress }) => {
      this.personAddress = personAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
