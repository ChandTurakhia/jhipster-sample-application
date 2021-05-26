import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonDetails } from '../person-details.model';

@Component({
  selector: 'jhi-person-details-detail',
  templateUrl: './person-details-detail.component.html',
})
export class PersonDetailsDetailComponent implements OnInit {
  personDetails: IPersonDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personDetails }) => {
      this.personDetails = personDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
