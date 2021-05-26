import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonAddress } from '../person-address.model';
import { PersonAddressService } from '../service/person-address.service';

@Component({
  templateUrl: './person-address-delete-dialog.component.html',
})
export class PersonAddressDeleteDialogComponent {
  personAddress?: IPersonAddress;

  constructor(protected personAddressService: PersonAddressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
