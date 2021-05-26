import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonAddress, getPersonAddressIdentifier } from '../person-address.model';

export type EntityResponseType = HttpResponse<IPersonAddress>;
export type EntityArrayResponseType = HttpResponse<IPersonAddress[]>;

@Injectable({ providedIn: 'root' })
export class PersonAddressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-addresses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personAddress: IPersonAddress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAddress);
    return this.http
      .post<IPersonAddress>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(personAddress: IPersonAddress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAddress);
    return this.http
      .put<IPersonAddress>(`${this.resourceUrl}/${getPersonAddressIdentifier(personAddress) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(personAddress: IPersonAddress): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personAddress);
    return this.http
      .patch<IPersonAddress>(`${this.resourceUrl}/${getPersonAddressIdentifier(personAddress) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersonAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonAddress[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonAddressToCollectionIfMissing(
    personAddressCollection: IPersonAddress[],
    ...personAddressesToCheck: (IPersonAddress | null | undefined)[]
  ): IPersonAddress[] {
    const personAddresses: IPersonAddress[] = personAddressesToCheck.filter(isPresent);
    if (personAddresses.length > 0) {
      const personAddressCollectionIdentifiers = personAddressCollection.map(
        personAddressItem => getPersonAddressIdentifier(personAddressItem)!
      );
      const personAddressesToAdd = personAddresses.filter(personAddressItem => {
        const personAddressIdentifier = getPersonAddressIdentifier(personAddressItem);
        if (personAddressIdentifier == null || personAddressCollectionIdentifiers.includes(personAddressIdentifier)) {
          return false;
        }
        personAddressCollectionIdentifiers.push(personAddressIdentifier);
        return true;
      });
      return [...personAddressesToAdd, ...personAddressCollection];
    }
    return personAddressCollection;
  }

  protected convertDateFromClient(personAddress: IPersonAddress): IPersonAddress {
    return Object.assign({}, personAddress, {
      validFrom: personAddress.validFrom?.isValid() ? personAddress.validFrom.toJSON() : undefined,
      validTo: personAddress.validTo?.isValid() ? personAddress.validTo.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validFrom = res.body.validFrom ? dayjs(res.body.validFrom) : undefined;
      res.body.validTo = res.body.validTo ? dayjs(res.body.validTo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((personAddress: IPersonAddress) => {
        personAddress.validFrom = personAddress.validFrom ? dayjs(personAddress.validFrom) : undefined;
        personAddress.validTo = personAddress.validTo ? dayjs(personAddress.validTo) : undefined;
      });
    }
    return res;
  }
}
