import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocationHeader, getLocationHeaderIdentifier } from '../location-header.model';

export type EntityResponseType = HttpResponse<ILocationHeader>;
export type EntityArrayResponseType = HttpResponse<ILocationHeader[]>;

@Injectable({ providedIn: 'root' })
export class LocationHeaderService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/location-headers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(locationHeader: ILocationHeader): Observable<EntityResponseType> {
    return this.http.post<ILocationHeader>(this.resourceUrl, locationHeader, { observe: 'response' });
  }

  update(locationHeader: ILocationHeader): Observable<EntityResponseType> {
    return this.http.put<ILocationHeader>(`${this.resourceUrl}/${getLocationHeaderIdentifier(locationHeader) as number}`, locationHeader, {
      observe: 'response',
    });
  }

  partialUpdate(locationHeader: ILocationHeader): Observable<EntityResponseType> {
    return this.http.patch<ILocationHeader>(
      `${this.resourceUrl}/${getLocationHeaderIdentifier(locationHeader) as number}`,
      locationHeader,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocationHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocationHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocationHeaderToCollectionIfMissing(
    locationHeaderCollection: ILocationHeader[],
    ...locationHeadersToCheck: (ILocationHeader | null | undefined)[]
  ): ILocationHeader[] {
    const locationHeaders: ILocationHeader[] = locationHeadersToCheck.filter(isPresent);
    if (locationHeaders.length > 0) {
      const locationHeaderCollectionIdentifiers = locationHeaderCollection.map(
        locationHeaderItem => getLocationHeaderIdentifier(locationHeaderItem)!
      );
      const locationHeadersToAdd = locationHeaders.filter(locationHeaderItem => {
        const locationHeaderIdentifier = getLocationHeaderIdentifier(locationHeaderItem);
        if (locationHeaderIdentifier == null || locationHeaderCollectionIdentifiers.includes(locationHeaderIdentifier)) {
          return false;
        }
        locationHeaderCollectionIdentifiers.push(locationHeaderIdentifier);
        return true;
      });
      return [...locationHeadersToAdd, ...locationHeaderCollection];
    }
    return locationHeaderCollection;
  }
}
