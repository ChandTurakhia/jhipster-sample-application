import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonDetails } from '../person-details.model';
import { PersonDetailsService } from '../service/person-details.service';
import { PersonDetailsDeleteDialogComponent } from '../delete/person-details-delete-dialog.component';

@Component({
  selector: 'jhi-person-details',
  templateUrl: './person-details.component.html',
})
export class PersonDetailsComponent implements OnInit {
  personDetails?: IPersonDetails[];
  isLoading = false;

  constructor(protected personDetailsService: PersonDetailsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personDetailsService.query().subscribe(
      (res: HttpResponse<IPersonDetails[]>) => {
        this.isLoading = false;
        this.personDetails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonDetails): number {
    return item.id!;
  }

  delete(personDetails: IPersonDetails): void {
    const modalRef = this.modalService.open(PersonDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personDetails = personDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
