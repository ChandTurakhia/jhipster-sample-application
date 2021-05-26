import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAddressHeader, AddressHeader } from '../address-header.model';
import { AddressHeaderService } from '../service/address-header.service';

@Injectable({ providedIn: 'root' })
export class AddressHeaderRoutingResolveService implements Resolve<IAddressHeader> {
  constructor(protected service: AddressHeaderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAddressHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((addressHeader: HttpResponse<AddressHeader>) => {
          if (addressHeader.body) {
            return of(addressHeader.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AddressHeader());
  }
}
