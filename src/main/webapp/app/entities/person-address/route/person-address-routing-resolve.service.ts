import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonAddress, PersonAddress } from '../person-address.model';
import { PersonAddressService } from '../service/person-address.service';

@Injectable({ providedIn: 'root' })
export class PersonAddressRoutingResolveService implements Resolve<IPersonAddress> {
  constructor(protected service: PersonAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personAddress: HttpResponse<PersonAddress>) => {
          if (personAddress.body) {
            return of(personAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonAddress());
  }
}
