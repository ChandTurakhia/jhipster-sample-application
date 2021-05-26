import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocationHeader, LocationHeader } from '../location-header.model';

import { LocationHeaderService } from './location-header.service';

describe('Service Tests', () => {
  describe('LocationHeader Service', () => {
    let service: LocationHeaderService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocationHeader;
    let expectedResult: ILocationHeader | ILocationHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocationHeaderService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        latitude: 0,
        longitude: 0,
        elevation: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LocationHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LocationHeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LocationHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            latitude: 1,
            longitude: 1,
            elevation: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LocationHeader', () => {
        const patchObject = Object.assign(
          {
            latitude: 1,
            longitude: 1,
            elevation: 1,
          },
          new LocationHeader()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LocationHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            latitude: 1,
            longitude: 1,
            elevation: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LocationHeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocationHeaderToCollectionIfMissing', () => {
        it('should add a LocationHeader to an empty array', () => {
          const locationHeader: ILocationHeader = { id: 123 };
          expectedResult = service.addLocationHeaderToCollectionIfMissing([], locationHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationHeader);
        });

        it('should not add a LocationHeader to an array that contains it', () => {
          const locationHeader: ILocationHeader = { id: 123 };
          const locationHeaderCollection: ILocationHeader[] = [
            {
              ...locationHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocationHeaderToCollectionIfMissing(locationHeaderCollection, locationHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LocationHeader to an array that doesn't contain it", () => {
          const locationHeader: ILocationHeader = { id: 123 };
          const locationHeaderCollection: ILocationHeader[] = [{ id: 456 }];
          expectedResult = service.addLocationHeaderToCollectionIfMissing(locationHeaderCollection, locationHeader);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationHeader);
        });

        it('should add only unique LocationHeader to an array', () => {
          const locationHeaderArray: ILocationHeader[] = [{ id: 123 }, { id: 456 }, { id: 10197 }];
          const locationHeaderCollection: ILocationHeader[] = [{ id: 123 }];
          expectedResult = service.addLocationHeaderToCollectionIfMissing(locationHeaderCollection, ...locationHeaderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const locationHeader: ILocationHeader = { id: 123 };
          const locationHeader2: ILocationHeader = { id: 456 };
          expectedResult = service.addLocationHeaderToCollectionIfMissing([], locationHeader, locationHeader2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationHeader);
          expect(expectedResult).toContain(locationHeader2);
        });

        it('should accept null and undefined values', () => {
          const locationHeader: ILocationHeader = { id: 123 };
          expectedResult = service.addLocationHeaderToCollectionIfMissing([], null, locationHeader, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationHeader);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
