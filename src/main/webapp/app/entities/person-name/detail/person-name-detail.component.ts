import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonName } from '../person-name.model';

@Component({
  selector: 'jhi-person-name-detail',
  templateUrl: './person-name-detail.component.html',
})
export class PersonNameDetailComponent implements OnInit {
  personName: IPersonName | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personName }) => {
      this.personName = personName;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
