import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationHeader } from '../location-header.model';
import { LocationHeaderService } from '../service/location-header.service';

@Component({
  templateUrl: './location-header-delete-dialog.component.html',
})
export class LocationHeaderDeleteDialogComponent {
  locationHeader?: ILocationHeader;

  constructor(protected locationHeaderService: LocationHeaderService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locationHeaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
