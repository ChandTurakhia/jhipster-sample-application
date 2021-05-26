import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonDetails } from '../person-details.model';
import { PersonDetailsService } from '../service/person-details.service';

@Component({
  templateUrl: './person-details-delete-dialog.component.html',
})
export class PersonDetailsDeleteDialogComponent {
  personDetails?: IPersonDetails;

  constructor(protected personDetailsService: PersonDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
