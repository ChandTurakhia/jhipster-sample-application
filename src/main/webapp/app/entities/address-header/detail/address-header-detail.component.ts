import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAddressHeader } from '../address-header.model';

@Component({
  selector: 'jhi-address-header-detail',
  templateUrl: './address-header-detail.component.html',
})
export class AddressHeaderDetailComponent implements OnInit {
  addressHeader: IAddressHeader | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ addressHeader }) => {
      this.addressHeader = addressHeader;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
