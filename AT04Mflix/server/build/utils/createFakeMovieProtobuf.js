"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createFakeMovieProtobuf = void 0;
var movies_pb_1 = require("../generated/movies_pb");
function createFakeMovieProtobuf() {
    var fakeProtoMovie = new movies_pb_1.Movie();
    var genre = new movies_pb_1.Genre();
    genre.setName("Genero teste 1");
    var cast = new movies_pb_1.Cast();
    cast.setActor("James Cameron");
    var country = new movies_pb_1.Country();
    country.setName("country");
    var director = new movies_pb_1.Director();
    director.setName("James Cameron Director");
    var language = new movies_pb_1.Language();
    language.setName("Portugues");
    var writer = new movies_pb_1.Writer();
    writer.setName("Edson Arantes do Nascimento");
    fakeProtoMovie.setPlot("plot");
    fakeProtoMovie.setGenresList([genre]);
    fakeProtoMovie.setRuntime(1);
    fakeProtoMovie.setCastList([cast]);
    fakeProtoMovie.setNumMflixComments(20);
    fakeProtoMovie.setTitle("Titulo");
    fakeProtoMovie.setFullplot("fullplot");
    fakeProtoMovie.setCountriesList([country]);
    fakeProtoMovie.setReleased("1893-05-09T00:00:00.000Z");
    fakeProtoMovie.setDirectorsList([director]);
    fakeProtoMovie.setRated("UNRATED");
    fakeProtoMovie.setLastupdated("2015-08-26 00:03:50.133000000");
    fakeProtoMovie.setYear("2023");
    fakeProtoMovie.setType("movie");
    fakeProtoMovie.setLanguagesList([language]);
    fakeProtoMovie.setWritersList([writer]);
    return fakeProtoMovie;
}
exports.createFakeMovieProtobuf = createFakeMovieProtobuf;
