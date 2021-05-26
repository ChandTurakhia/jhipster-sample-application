import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonLanguage } from '../person-language.model';

@Component({
  selector: 'jhi-person-language-detail',
  templateUrl: './person-language-detail.component.html',
})
export class PersonLanguageDetailComponent implements OnInit {
  personLanguage: IPersonLanguage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personLanguage }) => {
      this.personLanguage = personLanguage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
