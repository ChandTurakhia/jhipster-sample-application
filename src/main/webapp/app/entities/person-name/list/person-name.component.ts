import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonName } from '../person-name.model';
import { PersonNameService } from '../service/person-name.service';
import { PersonNameDeleteDialogComponent } from '../delete/person-name-delete-dialog.component';

@Component({
  selector: 'jhi-person-name',
  templateUrl: './person-name.component.html',
})
export class PersonNameComponent implements OnInit {
  personNames?: IPersonName[];
  isLoading = false;

  constructor(protected personNameService: PersonNameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personNameService.query().subscribe(
      (res: HttpResponse<IPersonName[]>) => {
        this.isLoading = false;
        this.personNames = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonName): number {
    return item.id!;
  }

  delete(personName: IPersonName): void {
    const modalRef = this.modalService.open(PersonNameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personName = personName;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
