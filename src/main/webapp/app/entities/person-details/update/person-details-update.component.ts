import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonDetails, PersonDetails } from '../person-details.model';
import { PersonDetailsService } from '../service/person-details.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-person-details-update',
  templateUrl: './person-details-update.component.html',
})
export class PersonDetailsUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    maritalTypeStatusCode: [],
    raceEthinicityCode: [],
    citizenshipStatusCode: [],
    pregnant: [],
    childrenCount: [],
    height: [],
    weight: [],
    person: [],
  });

  constructor(
    protected personDetailsService: PersonDetailsService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personDetails }) => {
      this.updateForm(personDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personDetails = this.createFromForm();
    if (personDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.personDetailsService.update(personDetails));
    } else {
      this.subscribeToSaveResponse(this.personDetailsService.create(personDetails));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonDetails>>): void {
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

  protected updateForm(personDetails: IPersonDetails): void {
    this.editForm.patchValue({
      id: personDetails.id,
      maritalTypeStatusCode: personDetails.maritalTypeStatusCode,
      raceEthinicityCode: personDetails.raceEthinicityCode,
      citizenshipStatusCode: personDetails.citizenshipStatusCode,
      pregnant: personDetails.pregnant,
      childrenCount: personDetails.childrenCount,
      height: personDetails.height,
      weight: personDetails.weight,
      person: personDetails.person,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, personDetails.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'persondetails-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));
  }

  protected createFromForm(): IPersonDetails {
    return {
      ...new PersonDetails(),
      id: this.editForm.get(['id'])!.value,
      maritalTypeStatusCode: this.editForm.get(['maritalTypeStatusCode'])!.value,
      raceEthinicityCode: this.editForm.get(['raceEthinicityCode'])!.value,
      citizenshipStatusCode: this.editForm.get(['citizenshipStatusCode'])!.value,
      pregnant: this.editForm.get(['pregnant'])!.value,
      childrenCount: this.editForm.get(['childrenCount'])!.value,
      height: this.editForm.get(['height'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
