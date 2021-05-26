import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersonLanguage, PersonLanguage } from '../person-language.model';
import { PersonLanguageService } from '../service/person-language.service';

@Component({
  selector: 'jhi-person-language-update',
  templateUrl: './person-language-update.component.html',
})
export class PersonLanguageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    languageCode: [],
    languageUsageCode: [],
    preferredLanguage: [],
  });

  constructor(
    protected personLanguageService: PersonLanguageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personLanguage }) => {
      this.updateForm(personLanguage);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personLanguage = this.createFromForm();
    if (personLanguage.id !== undefined) {
      this.subscribeToSaveResponse(this.personLanguageService.update(personLanguage));
    } else {
      this.subscribeToSaveResponse(this.personLanguageService.create(personLanguage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonLanguage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(personLanguage: IPersonLanguage): void {
    this.editForm.patchValue({
      id: personLanguage.id,
      languageCode: personLanguage.languageCode,
      languageUsageCode: personLanguage.languageUsageCode,
      preferredLanguage: personLanguage.preferredLanguage,
    });
  }

  protected createFromForm(): IPersonLanguage {
    return {
      ...new PersonLanguage(),
      id: this.editForm.get(['id'])!.value,
      languageCode: this.editForm.get(['languageCode'])!.value,
      languageUsageCode: this.editForm.get(['languageUsageCode'])!.value,
      preferredLanguage: this.editForm.get(['preferredLanguage'])!.value,
    };
  }
}
