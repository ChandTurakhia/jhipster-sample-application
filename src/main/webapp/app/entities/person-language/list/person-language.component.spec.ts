import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonLanguageService } from '../service/person-language.service';

import { PersonLanguageComponent } from './person-language.component';

describe('Component Tests', () => {
  describe('PersonLanguage Management Component', () => {
    let comp: PersonLanguageComponent;
    let fixture: ComponentFixture<PersonLanguageComponent>;
    let service: PersonLanguageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonLanguageComponent],
      })
        .overrideTemplate(PersonLanguageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonLanguageComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PersonLanguageService);

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
      expect(comp.personLanguages?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
