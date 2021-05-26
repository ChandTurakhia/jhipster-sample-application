import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonDetailsService } from '../service/person-details.service';

import { PersonDetailsComponent } from './person-details.component';

describe('Component Tests', () => {
  describe('PersonDetails Management Component', () => {
    let comp: PersonDetailsComponent;
    let fixture: ComponentFixture<PersonDetailsComponent>;
    let service: PersonDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonDetailsComponent],
      })
        .overrideTemplate(PersonDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PersonDetailsService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.personDetails?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
