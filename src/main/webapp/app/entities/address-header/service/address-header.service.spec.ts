import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAddressHeader, AddressHeader } from '../address-header.model';

import { AddressHeaderService } from './address-header.service';

describe('Service Tests', () => {
  describe('AddressHeader Service', () => {
    let service: AddressHeaderService;
    let httpMock: HttpTestingController;
    let elemDefault: IAddressHeader;
    let expectedResult: IAddressHeader | IAddressHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AddressHeaderService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        typeCode: 'AAAAAAA',
        standardized: false,
        addressLine1: 'AAAAAAA',
        addressLine2: 'AAAAAAA',
        addressLine3: 'AAAAAAA',
        cityName: 'AAAAAAA',
        countyName: 'AAAAAAA',
        stateCode: 'AAAAAAA',
        zipCode: 'AAAAAAA',
        countryName: 'AAAAAAA',
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

      it('should create a AddressHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AddressHeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AddressHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeCode: 'BBBBBB',
            standardized: true,
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            addressLine3: 'BBBBBB',
            cityName: 'BBBBBB',
            countyName: 'BBBBBB',
            stateCode: 'BBBBBB',
            zipCode: 'BBBBBB',
            countryName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AddressHeader', () => {
        const patchObject = Object.assign(
          {
            typeCode: 'BBBBBB',
            addressLine2: 'BBBBBB',
            addressLine3: 'BBBBBB',
            cityName: 'BBBBBB',
            countyName: 'BBBBBB',
            stateCode: 'BBBBBB',
            zipCode: 'BBBBBB',
            countryName: 'BBBBBB',
          },
          new AddressHeader()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AddressHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeCode: 'BBBBBB',
            standardized: true,
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            addressLine3: 'BBBBBB',
            cityName: 'BBBBBB',
            countyName: 'BBBBBB',
            stateCode: 'BBBBBB',
            zipCode: 'BBBBBB',
            countryName: 'BBBBBB',
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

      it('should delete a AddressHeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAddressHeaderToCollectionIfMissing', () => {
        it('should add a AddressHeader to an empty array', () => {
          const addressHeader: IAddressHeader = { id: 123 };
          expectedResult = service.addAddressHeaderToCollectionIfMissing([], addressHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(addressHeader);
        });

        it('should not add a AddressHeader to an array that contains it', () => {
          const addressHeader: IAddressHeader = { id: 123 };
          const addressHeaderCollection: IAddressHeader[] = [
            {
              ...addressHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addAddressHeaderToCollectionIfMissing(addressHeaderCollection, addressHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AddressHeader to an array that doesn't contain it", () => {
          const addressHeader: IAddressHeader = { id: 123 };
          const addressHeaderCollection: IAddressHeader[] = [{ id: 456 }];
          expectedResult = service.addAddressHeaderToCollectionIfMissing(addressHeaderCollection, addressHeader);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(addressHeader);
        });

        it('should add only unique AddressHeader to an array', () => {
          const addressHeaderArray: IAddressHeader[] = [{ id: 123 }, { id: 456 }, { id: 37767 }];
          const addressHeaderCollection: IAddressHeader[] = [{ id: 123 }];
          expectedResult = service.addAddressHeaderToCollectionIfMissing(addressHeaderCollection, ...addressHeaderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const addressHeader: IAddressHeader = { id: 123 };
          const addressHeader2: IAddressHeader = { id: 456 };
          expectedResult = service.addAddressHeaderToCollectionIfMissing([], addressHeader, addressHeader2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(addressHeader);
          expect(expectedResult).toContain(addressHeader2);
        });

        it('should accept null and undefined values', () => {
          const addressHeader: IAddressHeader = { id: 123 };
          expectedResult = service.addAddressHeaderToCollectionIfMissing([], null, addressHeader, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(addressHeader);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
