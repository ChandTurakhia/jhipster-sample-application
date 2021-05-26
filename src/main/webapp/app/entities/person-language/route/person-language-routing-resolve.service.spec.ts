jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonLanguage, PersonLanguage } from '../person-language.model';
import { PersonLanguageService } from '../service/person-language.service';

import { PersonLanguageRoutingResolveService } from './person-language-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonLanguage routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonLanguageRoutingResolveService;
    let service: PersonLanguageService;
    let resultPersonLanguage: IPersonLanguage | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonLanguageRoutingResolveService);
      service = TestBed.inject(PersonLanguageService);
      resultPersonLanguage = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonLanguage returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonLanguage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonLanguage).toEqual({ id: 123 });
      });

      it('should return new IPersonLanguage if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonLanguage = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonLanguage).toEqual(new PersonLanguage());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonLanguage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonLanguage).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
