import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAddressHeader, getAddressHeaderIdentifier } from '../address-header.model';

export type EntityResponseType = HttpResponse<IAddressHeader>;
export type EntityArrayResponseType = HttpResponse<IAddressHeader[]>;

@Injectable({ providedIn: 'root' })
export class AddressHeaderService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/address-headers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(addressHeader: IAddressHeader): Observable<EntityResponseType> {
    return this.http.post<IAddressHeader>(this.resourceUrl, addressHeader, { observe: 'response' });
  }

  update(addressHeader: IAddressHeader): Observable<EntityResponseType> {
    return this.http.put<IAddressHeader>(`${this.resourceUrl}/${getAddressHeaderIdentifier(addressHeader) as number}`, addressHeader, {
      observe: 'response',
    });
  }

  partialUpdate(addressHeader: IAddressHeader): Observable<EntityResponseType> {
    return this.http.patch<IAddressHeader>(`${this.resourceUrl}/${getAddressHeaderIdentifier(addressHeader) as number}`, addressHeader, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAddressHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAddressHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAddressHeaderToCollectionIfMissing(
    addressHeaderCollection: IAddressHeader[],
    ...addressHeadersToCheck: (IAddressHeader | null | undefined)[]
  ): IAddressHeader[] {
    const addressHeaders: IAddressHeader[] = addressHeadersToCheck.filter(isPresent);
    if (addressHeaders.length > 0) {
      const addressHeaderCollectionIdentifiers = addressHeaderCollection.map(
        addressHeaderItem => getAddressHeaderIdentifier(addressHeaderItem)!
      );
      const addressHeadersToAdd = addressHeaders.filter(addressHeaderItem => {
        const addressHeaderIdentifier = getAddressHeaderIdentifier(addressHeaderItem);
        if (addressHeaderIdentifier == null || addressHeaderCollectionIdentifiers.includes(addressHeaderIdentifier)) {
          return false;
        }
        addressHeaderCollectionIdentifiers.push(addressHeaderIdentifier);
        return true;
      });
      return [...addressHeadersToAdd, ...addressHeaderCollection];
    }
    return addressHeaderCollection;
  }
}
