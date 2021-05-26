import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocationHeader, LocationHeader } from '../location-header.model';
import { LocationHeaderService } from '../service/location-header.service';
import { IAddressHeader } from 'app/entities/address-header/address-header.model';
import { AddressHeaderService } from 'app/entities/address-header/service/address-header.service';

@Component({
  selector: 'jhi-location-header-update',
  templateUrl: './location-header-update.component.html',
})
export class LocationHeaderUpdateComponent implements OnInit {
  isSaving = false;

  addressHeadersCollection: IAddressHeader[] = [];

  editForm = this.fb.group({
    id: [],
    latitude: [],
    longitude: [],
    elevation: [],
    addressHeader: [],
  });

  constructor(
    protected locationHeaderService: LocationHeaderService,
    protected addressHeaderService: AddressHeaderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationHeader }) => {
      this.updateForm(locationHeader);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locationHeader = this.createFromForm();
    if (locationHeader.id !== undefined) {
      this.subscribeToSaveResponse(this.locationHeaderService.update(locationHeader));
    } else {
      this.subscribeToSaveResponse(this.locationHeaderService.create(locationHeader));
    }
  }

  trackAddressHeaderById(index: number, item: IAddressHeader): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocationHeader>>): void {
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

  protected updateForm(locationHeader: ILocationHeader): void {
    this.editForm.patchValue({
      id: locationHeader.id,
      latitude: locationHeader.latitude,
      longitude: locationHeader.longitude,
      elevation: locationHeader.elevation,
      addressHeader: locationHeader.addressHeader,
    });

    this.addressHeadersCollection = this.addressHeaderService.addAddressHeaderToCollectionIfMissing(
      this.addressHeadersCollection,
      locationHeader.addressHeader
    );
  }

  protected loadRelationshipsOptions(): void {
    this.addressHeaderService
      .query({ filter: 'locationheader-is-null' })
      .pipe(map((res: HttpResponse<IAddressHeader[]>) => res.body ?? []))
      .pipe(
        map((addressHeaders: IAddressHeader[]) =>
          this.addressHeaderService.addAddressHeaderToCollectionIfMissing(addressHeaders, this.editForm.get('addressHeader')!.value)
        )
      )
      .subscribe((addressHeaders: IAddressHeader[]) => (this.addressHeadersCollection = addressHeaders));
  }

  protected createFromForm(): ILocationHeader {
    return {
      ...new LocationHeader(),
      id: this.editForm.get(['id'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      elevation: this.editForm.get(['elevation'])!.value,
      addressHeader: this.editForm.get(['addressHeader'])!.value,
    };
  }
}
