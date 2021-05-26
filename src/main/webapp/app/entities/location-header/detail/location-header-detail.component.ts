import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocationHeader } from '../location-header.model';

@Component({
  selector: 'jhi-location-header-detail',
  templateUrl: './location-header-detail.component.html',
})
export class LocationHeaderDetailComponent implements OnInit {
  locationHeader: ILocationHeader | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationHeader }) => {
      this.locationHeader = locationHeader;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
