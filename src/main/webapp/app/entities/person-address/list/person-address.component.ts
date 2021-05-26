import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonAddress } from '../person-address.model';
import { PersonAddressService } from '../service/person-address.service';
import { PersonAddressDeleteDialogComponent } from '../delete/person-address-delete-dialog.component';

@Component({
  selector: 'jhi-person-address',
  templateUrl: './person-address.component.html',
})
export class PersonAddressComponent implements OnInit {
  personAddresses?: IPersonAddress[];
  isLoading = false;

  constructor(protected personAddressService: PersonAddressService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personAddressService.query().subscribe(
      (res: HttpResponse<IPersonAddress[]>) => {
        this.isLoading = false;
        this.personAddresses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonAddress): number {
    return item.id!;
  }

  delete(personAddress: IPersonAddress): void {
    const modalRef = this.modalService.open(PersonAddressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personAddress = personAddress;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
