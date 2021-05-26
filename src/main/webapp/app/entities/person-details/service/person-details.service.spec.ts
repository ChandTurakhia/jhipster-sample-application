import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonDetails, PersonDetails } from '../person-details.model';

import { PersonDetailsService } from './person-details.service';

describe('Service Tests', () => {
  describe('PersonDetails Service', () => {
    let service: PersonDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonDetails;
    let expectedResult: IPersonDetails | IPersonDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        maritalTypeStatusCode: 'AAAAAAA',
        raceEthinicityCode: 'AAAAAAA',
        citizenshipStatusCode: 'AAAAAAA',
        pregnant: false,
        childrenCount: 0,
        height: 'AAAAAAA',
        weight: 0,
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

      it('should create a PersonDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PersonDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maritalTypeStatusCode: 'BBBBBB',
            raceEthinicityCode: 'BBBBBB',
            citizenshipStatusCode: 'BBBBBB',
            pregnant: true,
            childrenCount: 1,
            height: 'BBBBBB',
            weight: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PersonDetails', () => {
        const patchObject = Object.assign(
          {
            maritalTypeStatusCode: 'BBBBBB',
            raceEthinicityCode: 'BBBBBB',
            citizenshipStatusCode: 'BBBBBB',
            childrenCount: 1,
          },
          new PersonDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maritalTypeStatusCode: 'BBBBBB',
            raceEthinicityCode: 'BBBBBB',
            citizenshipStatusCode: 'BBBBBB',
            pregnant: true,
            childrenCount: 1,
            height: 'BBBBBB',
            weight: 1,
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

      it('should delete a PersonDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonDetailsToCollectionIfMissing', () => {
        it('should add a PersonDetails to an empty array', () => {
          const personDetails: IPersonDetails = { id: 123 };
          expectedResult = service.addPersonDetailsToCollectionIfMissing([], personDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personDetails);
        });

        it('should not add a PersonDetails to an array that contains it', () => {
          const personDetails: IPersonDetails = { id: 123 };
          const personDetailsCollection: IPersonDetails[] = [
            {
              ...personDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonDetailsToCollectionIfMissing(personDetailsCollection, personDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonDetails to an array that doesn't contain it", () => {
          const personDetails: IPersonDetails = { id: 123 };
          const personDetailsCollection: IPersonDetails[] = [{ id: 456 }];
          expectedResult = service.addPersonDetailsToCollectionIfMissing(personDetailsCollection, personDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personDetails);
        });

        it('should add only unique PersonDetails to an array', () => {
          const personDetailsArray: IPersonDetails[] = [{ id: 123 }, { id: 456 }, { id: 69917 }];
          const personDetailsCollection: IPersonDetails[] = [{ id: 123 }];
          expectedResult = service.addPersonDetailsToCollectionIfMissing(personDetailsCollection, ...personDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personDetails: IPersonDetails = { id: 123 };
          const personDetails2: IPersonDetails = { id: 456 };
          expectedResult = service.addPersonDetailsToCollectionIfMissing([], personDetails, personDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personDetails);
          expect(expectedResult).toContain(personDetails2);
        });

        it('should accept null and undefined values', () => {
          const personDetails: IPersonDetails = { id: 123 };
          expectedResult = service.addPersonDetailsToCollectionIfMissing([], null, personDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
